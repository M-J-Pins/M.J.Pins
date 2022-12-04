from .auth.phone_auth import api_router as phone_auth_router
from .auth.phone_auth_request import api_router as phone_auth_request_router
from .cards.add_standard_card import api_router as add_standard_card_router
from .cards.add_unknown_card import api_router as add_unknown_card_router
from .cards.delete_card import api_router as delete_card_router
from .cards.my_cards import api_router as get_my_cards_router
from .cards.quick_cards import api_router as quick_cards_router
from .hard_test.hard_test import api_router as hard_test_router
from .shops.add_shop import api_router as add_shop_router
from .shops.add_similarity_data import api_router as add_similarity_data_router
from .shops.delete_shop import api_router as delete_shop_router
from .shops.get_categories import api_router as get_categories_router
from .shops.get_most_similar_shop import api_router as get_most_similar_shop_router
from .shops.get_shops import api_router as get_shops_router

list_of_routes = [
    hard_test_router,
    phone_auth_request_router,
    phone_auth_router,
    get_my_cards_router,
    add_unknown_card_router,
    add_standard_card_router,
    quick_cards_router,
    delete_card_router,
    add_shop_router,
    get_shops_router,
    delete_shop_router,
    get_categories_router,
    add_similarity_data_router,
    get_most_similar_shop_router,
]


__all__ = [
    "list_of_routes",
]
