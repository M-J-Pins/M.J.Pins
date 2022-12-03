from typing import List

from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Card
from quick_wallet.schemas.base import AuthorizedRequest
from quick_wallet.schemas.cards import CardResponse, CardListResponse
from quick_wallet.services.misc import JWTManager

api_router = APIRouter(prefix="/cards")


@api_router.get(
    "/my",
    response_model=CardListResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_401_UNAUTHORIZED: {
            "description": "Invalid or unknown token",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def auth_request_admin(
    request: AuthorizedRequest = Depends(),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает получает токен пользователя и отдает список его личных карт
    """

    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    db_cards: List[Card] = await Card.get_all(db, owner_id=user_id)
    response_cards: List[CardResponse] = [CardResponse.from_orm(card) for card in db_cards]
    response: CardListResponse = CardListResponse(cards=response_cards)

    return response
