import asyncio
from urllib.parse import urlparse

import uvicorn
from fastapi import FastAPI
from fastapi_pagination import add_pagination

from quick_wallet.config import ConfigSettings, get_settings
from quick_wallet.endpoints import list_of_routes


def bind_routes(application: FastAPI, setting: ConfigSettings) -> None:
    """
    Bind all routes to application
    """
    for route in list_of_routes:
        application.include_router(route, prefix=setting.PATH_PREFIX)


def get_app() -> FastAPI:
    """
    Creates application and all dependable objects
    """
    description = "API for Quick Wallet application"

    application = FastAPI(
        title="Quick Wallet",
        description=description,
        docs_url="/swagger",
        openapi_url="/openapi",
        version="0.0.1",
    )
    settings = get_settings()
    bind_routes(application, settings)
    add_pagination(application)
    application.state.settings = settings
    return application


app = get_app()

if __name__ == "__main__":
    settings_for_application = get_settings()
    config = uvicorn.Config(
        "quick_wallet.__main__:app",
        host=urlparse(settings_for_application.APP_HOST).netloc,
        port=settings_for_application.APP_PORT,
        reload=True,
        log_level="debug",
    )
    server = uvicorn.Server(config)
    server.run()
