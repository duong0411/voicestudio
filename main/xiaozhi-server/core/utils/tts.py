import os
import re
import sys
import importlib

from config.logger import setup_logging
from core.utils.textUtils import check_emoji

logger = setup_logging()

punctuation_set = {
    "，",
    ",",
    "。",
    ".",
    "！",
    "!",
    "“",
    "”",
    '"',
    "：",
    ":",
    "-",
    "－",
    "、",
    "[",
    "]",
    "【",
    "】",
    "~",
}

def create_instance(class_name, *args, **kwargs):

    if os.path.exists(os.path.join('core', 'providers', 'tts', f'{class_name}.py')):
        lib_name = f'core.providers.tts.{class_name}'
        if lib_name not in sys.modules:
            sys.modules[lib_name] = importlib.import_module(f'{lib_name}')
        return sys.modules[lib_name].TTSProvider(*args, **kwargs)

    raise ValueError(f"不支持的TTS类型: {class_name}，请检查该配置的type是否设置正确")


class MarkdownCleaner:
    """
     Markdown ： MarkdownCleaner.clean_markdown(text) 
    """

    NORMAL_FORMULA_CHARS = re.compile(r'[a-zA-Z\\^_{}\+\-\(\)\[\]=]')

    @staticmethod
    def _replace_inline_dollar(m: re.Match) -> str:
        """
         "$...$":
          -  =>  $
          -  (/) =>  "$...$"
        """
        content = m.group(1)
        if MarkdownCleaner.NORMAL_FORMULA_CHARS.search(content):
            return content
        else:
            return m.group(0)

    @staticmethod
    def _replace_table_block(match: re.Match) -> str:
        """
        ，。
        """
        block_text = match.group('table_block')
        lines = block_text.strip('\n').split('\n')

        parsed_table = []
        for line in lines:
            line_stripped = line.strip()
            if re.match(r'^\|\s*[-:]+\s*(\|\s*[-:]+\s*)+\|?$', line_stripped):
                continue
            columns = [col.strip() for col in line_stripped.split('|') if col.strip() != '']
            if columns:
                parsed_table.append(columns)

        if not parsed_table:
            return ""

        headers = parsed_table[0]
        data_rows = parsed_table[1:] if len(parsed_table) > 1 else []

        lines_for_tts = []
        if len(parsed_table) == 1:

            only_line_str = ", ".join(parsed_table[0])
            lines_for_tts.append(f"单行表格：{only_line_str}")
        else:
            lines_for_tts.append(f"表头是：{', '.join(headers)}")
            for i, row in enumerate(data_rows, start=1):
                row_str_list = []
                for col_index, cell_val in enumerate(row):
                    if col_index < len(headers):
                        row_str_list.append(f"{headers[col_index]} = {cell_val}")
                    else:
                        row_str_list.append(cell_val)
                lines_for_tts.append(f"第 {i} 行：{', '.join(row_str_list)}")

        return "\n".join(lines_for_tts) + "\n"



    REGEXES = [
        (re.compile(r'```.*?```', re.DOTALL), ''),
        (re.compile(r'^#+\s*', re.MULTILINE), ''),
        (re.compile(r'(\*\*|__)(.*?)\1'), r'\2'),
        (re.compile(r'(\*|_)(?=\S)(.*?)(?<=\S)\1'), r'\2'),
        (re.compile(r'!\[.*?\]\(.*?\)'), ''),
        (re.compile(r'\[(.*?)\]\(.*?\)'), r'\1'),
        (re.compile(r'^\s*>+\s*', re.MULTILINE), ''),
        (
            re.compile(r'(?P<table_block>(?:^[^\n]*\|[^\n]*\n)+)', re.MULTILINE),
            _replace_table_block
        ),
        (re.compile(r'^\s*[*+-]\s*', re.MULTILINE), '- '),
        (re.compile(r'\$\$.*?\$\$', re.DOTALL), ''),
        (
            re.compile(r'(?<![A-Za-z0-9])\$([^\n$]+)\$(?![A-Za-z0-9])'),
            _replace_inline_dollar
        ),
        (re.compile(r'\n{2,}'), '\n'),
    ]

    @staticmethod
    def clean_markdown(text: str) -> str:
        """
        ：， Markdown 
        """
        for regex, replacement in MarkdownCleaner.REGEXES:
            text = regex.sub(replacement, text)


        text = check_emoji(text)


        if text and all((c.isascii() or c.isspace() or c in punctuation_set) for c in text):

            return text

        return text.strip()

def convert_percentage_to_range(percentage, min_val, max_val, base_val=None):
    """
    (-100~100)

    Args:
        percentage:  (-100  100)
        min_val: 
        max_val: 
        base_val: （，）

    Returns:
        
    """
    percentage, min_val, max_val = float(percentage), float(min_val), float(max_val)
    base_val = float(base_val) if base_val is not None else (min_val + max_val) / 2

    if percentage < 0:

        result = base_val + (base_val - min_val) * (percentage / 100)
    else:

        result = base_val + (max_val - base_val) * (percentage / 100)


    return max(min_val, min(max_val, result))
