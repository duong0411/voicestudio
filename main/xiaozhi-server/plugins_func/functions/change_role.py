from plugins_func.register import register_function, ToolType, ActionResponse, Action
from config.logger import setup_logging
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from core.connection import ConnectionHandler

TAG = __name__
logger = setup_logging()

prompts = {
    "": """{{assistant_name}}(Lily)，，。
，。
，。
，。
，，。
，，。
，。""",
    "": """{{assistant_name}}，，，，。
，，。
，，，。""",
    "": """{{assistant_name}}8，。
，，。
，，、，。
，，。
，，。
，，，。
，，，。""",
}
change_role_function_desc = {
    "type": "function",
    "function": {
        "name": "change_role",
        "description": "//,：[,,]",
        "parameters": {
            "type": "object",
            "properties": {
                "role_name": {"type": "string", "description": ""},
                "role": {"type": "string", "description": ""},
            },
            "required": ["role", "role_name"],
        },
    },
}


@register_function("change_role", change_role_function_desc, ToolType.CHANGE_SYS_PROMPT)
def change_role(conn: "ConnectionHandler", role: str, role_name: str):
    """"""
    if role not in prompts:
        return ActionResponse(
            action=Action.RESPONSE, result="", response=""
        )
    new_prompt = prompts[role].replace("{{assistant_name}}", role_name)
    conn.change_system_prompt(new_prompt)
    logger.bind(tag=TAG).info(f":{role},:{role_name}")
    res = f",{role}{role_name}"
    return ActionResponse(action=Action.RESPONSE, result="", response=res)
