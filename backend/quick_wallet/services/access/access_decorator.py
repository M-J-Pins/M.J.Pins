from enum import Enum
from typing import Any
from uuid import UUID

from decorator import decorator
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.models import User
from quick_wallet.services.misc import JWTManager


class AccessLevel(Enum):
    UNAUTHORIZED = -1
    NO_ACCESS = 0
    OWNER = 1


class FunctionCallResult(Enum):
    SUCCESS = status.HTTP_200_OK
    UNAUTHORIZED = status.HTTP_401_UNAUTHORIZED
    NO_ACCESS = status.HTTP_403_FORBIDDEN


def authorized(func) -> (FunctionCallResult, Any):
    async def wrapper(
        db: AsyncSession,
        jwtoken: str,
        *args,
        **kwargs
    ):
        user_id = JWTManager().id_from_jwt(jwtoken)
        if user_id is None:
            return FunctionCallResult.UNAUTHORIZED, None
        db_user: User = await User.get(db, id=user_id)
        if db_user is None:
            return FunctionCallResult.UNAUTHORIZED, None

        result = await func(db, user_id, *args, **kwargs)
        return FunctionCallResult.SUCCESS, result

    return wrapper


# @decorator
# async def card_editor(required_access_level: AccessLevel) -> (FunctionCallResult, Any):
#     async def decor(func):
#         async def wrapper(
#             db: AsyncSession,
#             jwtoken: str,
#             *args,
#             card_id: UUID = None,
#             **kwargs
#         ):
#             access_level = await get_access_level(db, jwtoken, card_id)
#             if access_level < required_access_level:
#                 if access_level == AccessLevel.UNAUTHORIZED:
#                     return FunctionCallResult.UNAUTHORIZED, None
#                 if access_level == AccessLevel.NO_ACCESS:
#                     return FunctionCallResult.NO_ACCESS, None
#
#             result = await func(db, *args, **kwargs)
#             return FunctionCallResult.SUCCESS, result
#
#         return wrapper
#
#     return decor
#
#
# async def get_access_level(db: AsyncSession, jwtoken: str, target_entity_id: UUID):
#     admin_id = JWTManager().id_from_jwt(jwtoken)
#     if admin_id is None:
#         return AccessLevel.UNAUTHORIZED
#     admin: Admin = await Admin.get(db, id=admin_id)
#     if admin is None:
#         return AccessLevel.NO_ACCESS
#     if admin.app_owner:
#         return AccessLevel.ADMIN_OWNER
#
#     access_rule: AccessRule = await AccessRule.get(
#         db, admin_id=admin.id, entity_id=target_entity_id
#     )
#     if access_rule is not None:
#         if access_rule.full_access:
#             return AccessLevel.ADMIN_FULL_ACCESS
#         else:
#             return AccessLevel.ADMIN
#     else:
#         return AccessLevel.NO_ACCESS
