from datetime import datetime
import pytz


def check_datetime_expired(
        datetime_to_check: datetime,
        checking_moment_datetime: datetime = None
) -> bool:
    if checking_moment_datetime is None:
        checking_moment_datetime: datetime = datetime.utcnow().replace(tzinfo=pytz.utc)
    if datetime_to_check.timestamp() < checking_moment_datetime.timestamp():
        return True
    return False
