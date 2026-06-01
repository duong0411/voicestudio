import os
from config.config_loader import read_config, get_project_dir, load_config


config_file = "config.yaml"
config_file_valid = False


def check_config_file():
    global config_file_valid
    if config_file_valid:
        return
    """
    ，
    """
    config_file_path = get_project_dir() + config_file
    if not os.path.exists(config_file_path):
        raise FileNotFoundError(
            "config.yaml，"
        )


    config = load_config()
    if config.get("read_config_from_api", False):
        print("API")
        old_config_origin = read_config(config_file_path)
        if old_config_origin.get("selected_module") is not None:
            error_msg = "：\n"
            error_msg += "\n：\n"
            error_msg += "1、API mode: chỉ giữ lại manager-api config trong config.yaml\n"
            error_msg += "2、\n"
            raise ValueError(error_msg)
    config_file_valid = True
