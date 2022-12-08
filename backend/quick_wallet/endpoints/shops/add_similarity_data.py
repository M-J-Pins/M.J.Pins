from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.config.settings import get_settings
from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Shop, CardSimilarityData
from quick_wallet.schemas.base import BaseResponse
from quick_wallet.schemas.misc import AddSimilarityRequest

api_router = APIRouter(prefix="/shops")


@api_router.post(
    "/similarity",
    response_model=BaseResponse,
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
async def add_similarity_data(
    request: AddSimilarityRequest = Body(
        ...,
        example={
            "token": "Admin token",
            "shop_id": "bd65600d-8669-4903-8a14-af88203add38",
            "card_text": "Магнит косметик М",
            "card_color": "2271b3",
        },
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает новые данные о картах конкретного магазина
    Примерный текст на карте и/или основной цвет карты (hex format without #)
    Добавляет записи в таблицы shop_card_color, shop_card_text
    В дальнейшем эта информация будет использоваться для определения магазина по передней части карты
    """
    if request.token != get_settings().ADMIN_TOKEN:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN)

    if await Shop.get(db, id=request.shop_id) is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)

    await CardSimilarityData.create(db, shop_id=request.shop_id, text=request.card_text, color=request.card_color)

    return {"description": "Data was added successfully!"}
