import dayjs from "dayjs";
export const validateStartAndEndDate = (startDate, endDate, message, onValid, time = false, equal = true) => {
    if ((startDate == null || startDate === '') || (endDate == null || endDate === '')) {
      if (onValid != null) {
        onValid();
      }
      return true;
    }
    const yearPrefix = "2000-01-01 ";
    const startDateParsed = time ? dayjs(yearPrefix + startDate) : dayjs(startDate);
    const endDateParsed = time ? dayjs(yearPrefix + endDate) : dayjs(endDate);
    const datesDiffSecond = startDateParsed.diff(endDateParsed, 'second');
    let validationResult;
    if (equal) {
      validationResult = datesDiffSecond <= 0
    } else {
      validationResult = datesDiffSecond < 0
    }
    if (validationResult === false) {
      validationResult = message || 'startDate should not be greater than endDate';
    } else {
      if (onValid != null) {
        onValid();
      }
    }

    return validationResult;
}
export const validateStartAndEndTime = (startDate, endDate, message, onValid) => {
  return validateStartAndEndDate(startDate, endDate, message, onValid, true);
}
export const validateStartAndEndTimeNotEqual = (startDate, endDate, message, onValid) => {
  return validateStartAndEndDate(startDate, endDate, message, onValid, true, false);
}