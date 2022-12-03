from .auth.phone_auth import api_router as phone_auth_router
from .auth.phone_auth_request import api_router as phone_auth_request_router
from .cards.add_unknown_card import api_router as add_unknown_card_router
from .cards.add_standart_card import api_router as add_standart_card_router

list_of_routes = [
    phone_auth_request_router,
    phone_auth_router,
    add_unknown_card_router,
    add_standart_card_router,
]


__all__ = [
    "list_of_routes",
]