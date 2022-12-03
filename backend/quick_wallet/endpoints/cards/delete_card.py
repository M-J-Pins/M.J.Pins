from uuid import UUID

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Card
from quick_wallet.schemas.base import AuthorizedRequest, BaseResponse
from quick_wallet.services.misc import JWTManager

api_router = APIRouter(prefix="/cards")


@api_router.delete(
    "/{card_id}",
    response_model=BaseResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_401_UNAUTHORIZED: {
            "description": "Invalid or unknown token",
        },
        status.HTTP_403_FORBIDDEN: {
            "description": "Not card owner token",
        },
        status.HTTP_404_NOT_FOUND: {
            "description": "No card with such id was found",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def delete_card(
    card_id: UUID,
    request: AuthorizedRequest = Depends(),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает получает токен пользователя и id карты на удаление
    """

    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    db_card: Card = await Card.get(db, id=card_id)
    if db_card is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)

    if db_card.owner_id != user_id:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN)

    await Card.delete(db, id=card_id)
    return {"description": "Card was deleted successfully!"}
