from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.schemas.base import BaseResponse
from quick_wallet.schemas.wallets import CardWalletRequest
from quick_wallet.services.misc import JWTManager
from quick_wallet.services.wallets import WalletActionResult, WalletManager

api_router = APIRouter(prefix="/wallets")


@api_router.post(
    "/cards",
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
            "description": "No access",
        },
        status.HTTP_404_NOT_FOUND: {
            "description": "Card or wallet wasn't found",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def add_card_to_wallet(
    request: CardWalletRequest = Body(
        ...,
        example={
            "token": "<JWT>",
            "wallet_id": "<UUID>",
            "card_id": "<UUID>",
        },
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает id кошелька, id карточки
    Добавляет карточку в кошелек, если такая карта существует, у пользователя есть доступ к кошельку
    и он владелец карты
    """
    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    res: WalletActionResult = await WalletManager.add_card(
        db, user_id, request.wallet_id, request.card_id
    )
    return res.value
