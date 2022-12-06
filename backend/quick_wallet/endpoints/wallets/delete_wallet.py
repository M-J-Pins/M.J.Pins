from typing import List
from uuid import UUID

from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models.storage import Wallet
from quick_wallet.schemas.wallets import WalletId
from quick_wallet.services.misc import JWTManager
from quick_wallet.services.wallets import WalletManager, WalletActionResult
from quick_wallet.schemas.base import BaseResponse


api_router = APIRouter(prefix="/wallets")


@api_router.delete(
    "/{wallet_id}",
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
async def delete_wallet(
    request: WalletId = Depends(),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает id кошелька,
    Удаляет кошелек, если пользователь создатель
    Удаляет доступ к кошельку, если пользователь имеет доступ
    """
    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    wallet: Wallet = await Wallet.get(db, id=request.wallet_id, owner_id=user_id)
    if wallet is None:
        res: WalletActionResult = await WalletManager.leave_wallet(db, user_id, request.wallet_id)
    else:
        res: WalletActionResult = await WalletManager.delete_wallet(db, user_id, request.wallet_id)
    return res.value

