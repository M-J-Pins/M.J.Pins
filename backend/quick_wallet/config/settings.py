from os import environ
from typing import Any

from dotenv import load_dotenv
from pydantic import BaseSettings


class ConfigSettings(BaseSettings):
    """
    Settings for application
    Settings will be loaded from env
    """

    def __init__(self, **values: Any):
        super().__init__(**values)
        load_dotenv()

    ENV: str = environ.get("ENV", "local")
    PATH_PREFIX: str = environ.get("PATH_PREFIX", "/api/v1")
    APP_HOST: str = environ.get("APP_HOST", "http://127.0.0.1")
    APP_PORT: int = int(environ.get("APP_PORT", 8000))

    SMSAERO_EMAIL: str = environ.get("SMSAERO_EMAIL", "email")
    SMSAERO_KEY: str = environ.get("SMSAERO_KEY", "key")

    ADMIN_TOKEN: str = environ.get("ADMIN_TOKEN", "ADMIN_TOKEN")
    IMGBB_KEY: str = environ.get("IMGBB_KEY", "IMGBB_KEY")
    YANDEX_API_KEY: str = environ.get("YANDEX_API_KEY", "YANDEX_API_KEY")
    SEARCH_RADIUS: int = int(environ.get("SEARCH_RADIUS", 150))

    POSTGRES_DB: str = environ.get("POSTGRES_DB", "quick-wallet")
    POSTGRES_HOST: str = environ.get("POSTGRES_HOST", "localhost")
    POSTGRES_USER: str = environ.get("POSTGRES_USER", "user")
    POSTGRES_PORT: int = int(environ.get("POSTGRES_PORT"[-4:], 5432))
    POSTGRES_PASSWORD: str = environ.get("POSTGRES_PASSWORD", "password")
    DB_CONNECT_RETRY: int = environ.get("DB_CONNECT_RETRY", 20)
    DB_POOL_SIZE: int = environ.get("DB_POOL_SIZE", 15)

    @property
    def database_settings(self) -> dict:
        """
        Get all settings for connection with database
        """
        return {
            "database": self.POSTGRES_DB,
            "user": self.POSTGRES_USER,
            "password": self.POSTGRES_PASSWORD,
            "host": self.POSTGRES_HOST,
            "port": self.POSTGRES_PORT,
        }

    @property
    def database_uri(self) -> str:
        """
        Get uri for async connection with database
        """
        return "postgresql+asyncpg://{user}:{password}@{host}:{port}/{database}".format(
            **self.database_settings,
        )

    @property
    def sync_database_uri(self) -> str:
        """
        Get uri for sync connection with database
        """
        return "postgresql://{user}:{password}@{host}:{port}/{database}".format(
            **self.database_settings,
        )


def get_settings() -> ConfigSettings:
    return ConfigSettings()
