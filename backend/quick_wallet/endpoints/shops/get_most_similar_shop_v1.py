from typing import List
from uuid import UUID

from fastapi import APIRouter, Depends, File, HTTPException, UploadFile
from PIL import Image
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models.analysis import AddCardAction
from quick_wallet.database.models.storage import Shop
from quick_wallet.schemas.base import AuthorizedRequest
from quick_wallet.schemas.shops import MostSimilarShopResponse, ShopResponse
from quick_wallet.services.misc import AssetManager, JWTManager
from quick_wallet.services.shop_simillarity import ShopSimilarityManager

api_router = APIRouter(prefix="/shops")


@api_router.post(
    "/similarity/most/v1",
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
            "description": "Similar shop wasn't found",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def get_most_similar_shop_v1(
    request: AuthorizedRequest = Depends(),
    card_photo: UploadFile = File(...),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает фото передней части карты
    Возвращает наиболее вероятный магазин
    """
    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    image_name = AssetManager.save_image(card_photo.file.read(), gray_duplicate=True)
    image_gray: Image = AssetManager.get_gray_pil_img(image_name)
    card_text = ShopSimilarityManager.get_text_from_image(image_gray)
    card_color = ShopSimilarityManager.count_image_color(card_photo.file)

    db_shops: List[Shop] = await Shop.get_all(db)
    db_shops = [] if db_shops is None else db_shops
    shop_ids = [shop.id for shop in db_shops]
    most_similar_shop_id: UUID = await ShopSimilarityManager(
        1, 1
    ).find_the_most_similar_shop_v1(db, shop_ids, card_text, card_color)
    if most_similar_shop_id is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)
    else:
        most_similar_shop: Shop = await Shop.get(db, id=most_similar_shop_id)

    add_card_action: AddCardAction = await AddCardAction.create(
        db,
        image_name=image_name,
        program_predict_shop_id=most_similar_shop.id,
        program_predict_shop_name=most_similar_shop.name,
    )

    return MostSimilarShopResponse(
        **ShopResponse.from_orm(most_similar_shop).__dict__,
        add_card_action_id=add_card_action.id
    )
