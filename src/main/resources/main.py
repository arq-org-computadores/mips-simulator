import binascii
import json
import os
import sys
import typing
from dataclasses import dataclass

import dearpygui.dearpygui as dpg
from dearpygui._dearpygui import mvDir_Right


@dataclass(frozen=False)
class Instruction:
    hex_str: str
    assembly_str: str
    address: int


@dataclass(frozen=False)
class MemoryLocation:
    address: str
    value: int


INPUT_PATH = 'output/gui/'
FONT_PATH = "src/main/resources/fonts/roboto-regular.ttf"

JSON_LIST: typing.List[typing.Dict] = []
LIST_INDEX: int = 0

INSTRUCTIONS: typing.List[Instruction] = []
REGISTERS: typing.Dict[str, int] = {}
MEMORY: typing.List[MemoryLocation] = []

TEXT_BEGIN = 4194304
TEXT_END = 268435452
HIGHLIGHT_PC: int = 0


def _load_regs_mem():
    mem = JSON_LIST[LIST_INDEX]['mem']
    for addr in mem:
        i_addr = int(addr)
        if i_addr >= TEXT_BEGIN and i_addr <= TEXT_END:
            hex_str = hex(int(mem[addr]) & 0xffffffff)
            aux = hex_str[2:]
            hex_str = hex_str[0:2] + ''.join(["0"] * (8 - len(aux))) + aux
            INSTRUCTIONS.append(Instruction(hex_str=hex_str,
                                            assembly_str="",
                                            address=addr))
        else:
            MEMORY.append(MemoryLocation(address=addr,
                                         value=mem[addr]))

    regs = JSON_LIST[LIST_INDEX]['regs']
    for reg_i in regs:
        REGISTERS[reg_i] = regs[reg_i]


def _load_assembly():
    hex_str = JSON_LIST[LIST_INDEX]["hex"]
    assembly = JSON_LIST[LIST_INDEX]["text"]

    for inst in INSTRUCTIONS:
        if inst.hex_str == hex_str:
            inst.assembly_str = str(assembly)
            break


def _update_regs():
    global REGISTERS

    for i in range(32):
        id_ = f"${i}"
        tag = f"reg-{i}"
        value = REGISTERS[id_] if id_ in REGISTERS else 0
        dpg.set_value(tag, value)

    for id_ in ["pc", "hi", "lo"]:
        tag = f"reg-{id_}"
        value = REGISTERS[id_] if id_ in REGISTERS else 0
        dpg.set_value(tag, value)


def _update_memory():
    global MEMORY

    for mem in MEMORY:
        value = mem.value

        if value != 0:
            address = mem.address
            tag = f"mem-{address}"

            if dpg.does_item_exist(tag):
                dpg.set_value(tag, value)
            else:
                with dpg.table_row(parent="mem-table"):
                    dpg.add_text(address)
                    dpg.add_text(value, tag=tag)


def _update_assembly():
    global INSTRUCTIONS
    global HIGHLIGHT_PC

    for inst in INSTRUCTIONS:
        if inst.assembly_str != '':
            tag = f"assembly-{inst.address}"
            value = inst.assembly_str
            dpg.set_value(tag, value)

            if int(inst.address) == int(HIGHLIGHT_PC):
                dpg.configure_item(tag, color=[50, 168, 82, 255])
            else:
                dpg.configure_item(tag, color=[255, 255, 255, 255])


def load_data():
    global HIGHLIGHT_PC
    global LIST_INDEX
    global JSON_LIST

    i = 0
    LIST_INDEX = 0
    path = os.path.join(INPUT_PATH, f'{i}.json')

    while os.path.exists(path):
        with open(path) as json_file:
            JSON_LIST.insert(i, json.load(json_file))

        i += 1
        path = os.path.join(INPUT_PATH, f'{i}.json')

    _load_regs_mem()
    HIGHLIGHT_PC = REGISTERS['pc']


def move_next():
    global LIST_INDEX
    global JSON_LIST
    global REGISTERS
    global HIGHLIGHT_PC
    global _load_regs_mem
    global _load_assembly
    global _update_regs
    global _update_memory
    global _update_assembly

    if LIST_INDEX + 1 >= len(JSON_LIST):
        sys.exit(0)
        return

    LIST_INDEX += 1

    HIGHLIGHT_PC = REGISTERS['pc']
    _load_regs_mem()
    _load_assembly()

    _update_regs()
    _update_memory()
    _update_assembly()


if __name__ == '__main__':
    load_data()

    dpg.create_context()

    default_window_configs = {
        'no_resize': True,
        'no_move': True,
        'no_close': True,
        'no_collapse': True
    }

    with dpg.font_registry():
        default_font = dpg.add_font(FONT_PATH, 20)
        small_font = dpg.add_font(FONT_PATH, 15)

    dpg.bind_font(default_font)
    dpg.create_viewport(title='MIPS32 Simulator',
                        width=840,
                        height=720,
                        resizable=False,
                        decorated=True)

    with dpg.window(label="Opções",
                    width=260,
                    height=90,
                    pos=[580, 630],
                    **default_window_configs):
        dpg.add_button(label='Próxima Instrução',
                       width=260,
                       height=45,
                       callback=move_next)

    with dpg.window(label="Registradores",
                    width=260,
                    height=630,
                    pos=[580, 0],
                    **default_window_configs):
        with dpg.table(tag="reg-table",
                       header_row=False,
                       resizable=True,
                       policy=dpg.mvTable_SizingStretchProp,
                       borders_outerH=True,
                       borders_innerV=True,
                       borders_innerH=True,
                       borders_outerV=True):
            dpg.add_table_column(label="Número")
            dpg.add_table_column(label="Valor")

            for i in range(32):
                with dpg.table_row():
                    id_ = f"${i}"
                    dpg.add_text(id_)
                    dpg.add_text(
                        REGISTERS[id_] if id_ in REGISTERS else 0, tag=f"reg-{i}")

            with dpg.table_row():
                id_ = "pc"
                dpg.add_text(id_)
                dpg.add_text(
                    REGISTERS[id_] if id_ in REGISTERS else 0, tag="reg-pc")

            with dpg.table_row():
                id_ = "hi"
                dpg.add_text(id_)
                dpg.add_text(
                    REGISTERS[id_] if id_ in REGISTERS else 0, tag="reg-hi")

            with dpg.table_row():
                id_ = "lo"
                dpg.add_text(id_)
                dpg.add_text(
                    REGISTERS[id_] if id_ in REGISTERS else 0, tag="reg-lo")

            dpg.bind_font(small_font)

    with dpg.window(label="Memória",
                    width=580,
                    height=300,
                    pos=[0, 420],
                    **default_window_configs):
        with dpg.table(tag="mem-table",
                       header_row=True,
                       resizable=True,
                       policy=dpg.mvTable_SizingStretchProp,
                       borders_outerH=True,
                       borders_innerV=True,
                       borders_innerH=True,
                       borders_outerV=True):
            dpg.add_table_column(label="Endereço")
            dpg.add_table_column(label="Valor")

            for mem in MEMORY:
                with dpg.table_row():
                    dpg.add_text(mem.address)
                    dpg.add_text(mem.value, tag=f"mem-{mem.address}")

    with dpg.window(label="Principal",
                    width=580,
                    height=420,
                    pos=[0, 0],
                    **default_window_configs):
        with dpg.table(tag="main-table",
                       header_row=True,
                       resizable=False,
                       policy=dpg.mvTable_SizingStretchProp,
                       borders_outerH=True,
                       borders_innerV=True,
                       borders_innerH=True,
                       borders_outerV=True):
            dpg.add_table_column(label="Endereço", width=120, width_fixed=True)
            dpg.add_table_column(label="Linguagem de Máquina",
                                 width=120, width_fixed=True)
            dpg.add_table_column(label="Assembly", width=340)

            for inst in INSTRUCTIONS:
                with dpg.table_row():
                    dpg.add_text(inst.address)
                    dpg.add_text(inst.hex_str)
                    dpg.add_text(inst.assembly_str,
                                 tag=f"assembly-{inst.address}")

    dpg.setup_dearpygui()
    dpg.show_viewport()
    dpg.start_dearpygui()
    dpg.destroy_context()
