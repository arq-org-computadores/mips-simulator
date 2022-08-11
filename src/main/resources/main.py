import os
import json
import typing
import binascii

from dataclasses import dataclass

import dearpygui.dearpygui as dpg
from dearpygui._dearpygui import mvDir_Right


@dataclass(frozen=True)
class Instruction:
    hex_str: str
    assembly_str: str
    address: int


@dataclass(frozen=True)
class Register:
    id: str
    value: int


@dataclass(frozen=True)
class MemoryLocation:
    address: str
    value: int


INPUT_PATH = 'output/gui/'
FONT_PATH = "src/main/resources/fonts/roboto-regular.ttf"

JSON_LIST: typing.List[typing.Dict] = []
LIST_INDEX = None

INSTRUCTIONS: typing.List[Instruction] = []
REGISTERS: typing.List[Register] = []
MEMORY: typing.List[MemoryLocation] = []

TEXT_BEGIN = 4194304
TEXT_END = 268435452


def load_data():
    i = 0
    LIST_INDEX = -1
    path = os.path.join(INPUT_PATH, f'{i}.json')

    while os.path.exists(path):
        with open(path) as json_file:
            JSON_LIST.insert(i, json.load(json_file))

        i += 1
        path = os.path.join(INPUT_PATH, f'{i}.json')

    mem = JSON_LIST[0]['mem']
    for addr in mem:
        i_addr = int(addr)
        if i_addr >= TEXT_BEGIN and i_addr <= TEXT_END:
            hex_str = hex(int(mem[addr]) + 2**32)
            aux = hex_str[3:]
            hex_str = hex_str[0:2] + ''.join(["0"] * (8 - len(aux))) + aux
            INSTRUCTIONS.append(Instruction(hex_str=hex_str,
                                            assembly_str="",
                                            address=addr))
        else:
            MEMORY.append(MemoryLocation(address=addr,
                                         value=mem[addr]))


def move_next():
    print('Próxima instrução')
    pass


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
        with dpg.table(label="reg-table",
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
                    dpg.add_text(f"${i}")
                    dpg.add_text("0")

            with dpg.table_row():
                dpg.add_text("pc")
                dpg.add_text("0")

            with dpg.table_row():
                dpg.add_text("hi")
                dpg.add_text("0")

            with dpg.table_row():
                dpg.add_text("lo")
                dpg.add_text("0")

            dpg.bind_font(small_font)

    with dpg.window(label="Memória",
                    width=580,
                    height=300,
                    pos=[0, 420],
                    **default_window_configs):
        with dpg.table(label="mem-table",
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
                    dpg.add_text(mem.value)

    with dpg.window(label="Principal",
                    width=580,
                    height=420,
                    pos=[0, 0],
                    **default_window_configs):
        with dpg.table(label="main-table",
                       header_row=True,
                       resizable=True,
                       policy=dpg.mvTable_SizingStretchProp,
                       borders_outerH=True,
                       borders_innerV=True,
                       borders_innerH=True,
                       borders_outerV=True):
            dpg.add_table_column(label="Endereço")
            dpg.add_table_column(label="Linguagem de Máquina")
            dpg.add_table_column(label="Assembly")

            for inst in INSTRUCTIONS:
                with dpg.table_row():
                    dpg.add_text(inst.address)
                    dpg.add_text(inst.hex_str)
                    dpg.add_text(inst.assembly_str)

    dpg.setup_dearpygui()
    dpg.show_viewport()
    dpg.start_dearpygui()
    dpg.destroy_context()
