from uuid import UUID

from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.config.settings import get_settings
from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Shop
from quick_wallet.schemas.base import AuthorizedRequest, BaseResponse

api_router = APIRouter(prefix="/shops")


@api_router.delete(
    "/{shop_id}",
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
            "description": "Shop with such id wasn't found",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def delete_shop(
    shop_id: UUID,
    request: AuthorizedRequest = Depends(),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка удаляет магазин с id из url
    """
    if request.token != get_settings().ADMIN_TOKEN:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN)

    if await Shop.get(db, id=shop_id) is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)

    await Shop.delete(db, id=shop_id)
    return {"description": "Shop was deleted successfully!"}
