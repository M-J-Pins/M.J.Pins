from .auth.phone_auth_request import api_router as phone_auth_request_router
from .auth.phone_auth import api_router as phone_auth_router

list_of_routes = [
    phone_auth_request_router,
    phone_auth_router,
]


__all__ = [
    "list_of_routes",
]
