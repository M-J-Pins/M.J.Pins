from typing import List
from uuid import UUID

from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.schemas.wallets import AccessToWalletRequest
from quick_wallet.services.misc import JWTManager, ConvertManager
from quick_wallet.services.wallets import WalletManager, WalletActionResult
from quick_wallet.schemas.base import BaseResponse


api_router = APIRouter(prefix="/wallets")


@api_router.post(
    "/access",
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
async def give_access_to_wallet(
    request: AccessToWalletRequest = Body(
        ...,
        example={
            "token": "<JWT>",
            "user_to_give_access_phone": "<UUID>",
            "wallet_id": "<UUID>",
        }
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает id пользователя, которому надо дать доступ к кошельку
    и id кошелька
    Дает доступ, если запрос отправил владелец кошелька
    """
    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    user_to_give_access_id: UUID = await ConvertManager.phone2user_id(db, request.user_to_change_access_phone)
    res, access = await WalletManager.give_access(db, user_id, request.wallet_id, user_to_give_access_id)
    return res.value

