from typing import List

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Shop
from quick_wallet.schemas.shops import ShopListResponse, ShopResponse

api_router = APIRouter(prefix="/shops")


@api_router.get(
    "/",
    response_model=ShopListResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def get_shops(
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка возвращает список зарегистрированных магазинов
    """
    db_shops: List[Shop] = await Shop.get_all(db)
    if db_shops is None:
        return ShopListResponse()

    response_shops: List[ShopResponse] = [
        ShopResponse.from_orm(shop) for shop in db_shops
    ]
    response: ShopListResponse = ShopListResponse(shops=response_shops)

    return response
