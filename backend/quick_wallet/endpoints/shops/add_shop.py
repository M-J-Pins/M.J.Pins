from fastapi import APIRouter, Body, Depends, File, HTTPException, UploadFile
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.config.settings import get_settings
from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Shop
from quick_wallet.schemas.shops import AddShopRequest, ShopResponse
from quick_wallet.services.misc import AssetManager

api_router = APIRouter(prefix="/shops")


@api_router.post(
    "/add",
    response_model=ShopResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_403_FORBIDDEN: {
            "description": "Invalid or unknown admin token",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def add_shop(
    request: AddShopRequest = Depends(),
    icon: UploadFile = File(...),
    card_image: UploadFile = File(...),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает информацию о новом магазине в query params и изображения icon и card image
    Добавляет магазин в базу, генерирует ссылки для скачивания изображений
    Возвращает объект магазина
    """
    if request.token != get_settings().ADMIN_TOKEN:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN)

    icon_url = AssetManager().upload_image(await icon.read())
    card_image_url = AssetManager().upload_image(await card_image.read())
    new_shop: Shop = await Shop.create(
        db,
        name=request.name,
        map_search_string=request.map_search_string,
        icon_url=icon_url,
        category=request.category,
        card_image_url=card_image_url,
    )

    return ShopResponse.from_orm(new_shop)
