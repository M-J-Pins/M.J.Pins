import asyncio

import pytest
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession, AsyncEngine
from sqlalchemy.orm import sessionmaker


@pytest.fixture(scope="session")
def event_loop() -> asyncio.AbstractEventLoop:
    policy = asyncio.get_event_loop_policy()
    loop = policy.new_event_loop()
    yield loop
    loop.close()


@pytest.fixture(scope='session')
async def db_engine_async(settings_temp_db) -> AsyncEngine:
    engine = create_async_engine(settings_temp_db.database_uri, future=True)
    yield engine
    await engine.dispose()


@pytest.fixture(scope='session')
def db_session_factory_async(db_engine_async) -> sessionmaker:
    return sessionmaker(db_engine_async, class_=AsyncSession, expire_on_commit=False)


@pytest.fixture
async def db_session_async(db_session_factory_async) -> AsyncSession:
    async with db_session_factory_async() as session:
        yield session
