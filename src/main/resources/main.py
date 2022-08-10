import os
import json
import typing

import dearpygui.dearpygui as dpg
from dearpygui._dearpygui import mvDir_Right


INPUT_PATH = 'output/gui/'
FONT_PATH = "src/main/resources/fonts/roboto-regular.ttf"

INITIAL_JSON: typing.Dict = None
JSON_LIST: typing.List[typing.Dict] = None
LIST_INDEX = None

INSTRUCTIONS_HEX: typing.List[typing.Dict] = None


def load_data():
    with open(os.path.join(INPUT_PATH, '0.json')) as json_file:
        INITIAL_JSON = json.load(json_file)


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

    dpg.setup_dearpygui()
    dpg.show_viewport()
    dpg.start_dearpygui()
    dpg.destroy_context()
