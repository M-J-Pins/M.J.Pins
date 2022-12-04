from typing import List
from uuid import UUID

from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.config.settings import get_settings
from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Shop
from quick_wallet.schemas.misc import SimilarityRequest
from quick_wallet.schemas.shops import ShopResponse
from quick_wallet.services.misc import JWTManager, ShopSimilarityManager

api_router = APIRouter(prefix="/shops")


@api_router.post(
    "/similarity/most",
    response_model=ShopResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_403_FORBIDDEN: {
            "description": "Invalid or unknown admin token",
        },
        status.HTTP_404_NOT_FOUND: {
            "description": "Shop with such shop_id wasn't found",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def get_most_similar_shop(
    request: SimilarityRequest = Body(
        ...,
        example={
            "token": "Admin token",
            "card_text": "Магнит косметик М",
            "card_color": "2271b3",
        },
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает текст с карты и цвет
    Возвращает наиболее вероятный магазин
    """
    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    db_shops: List[Shop] = await Shop.get_all(db)
    db_shops = [] if db_shops is None else db_shops
    shop_ids = [shop.id for shop in db_shops]
    most_similar_shop_id: UUID = await ShopSimilarityManager(
        1, 1
    ).find_the_most_suitable_shop(db, shop_ids, request.card_text, request.card_color)
    if most_similar_shop_id is None:
        most_similar_shop: Shop = await Shop.get(db)
    else:
        most_similar_shop: Shop = await Shop.get(db, shop_id=most_similar_shop_id)

    return ShopResponse.from_orm(most_similar_shop)
