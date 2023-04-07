/*
 * based in rules of: react-hook-form 
 */

export const RULE_REQUIRED = { required: "Value is required" };
export const RULE_NUMBER_NO_DECIMAL = { pattern:{value: /^\d+$/, message: "Only numbers allowed"} };
export const RULE_POSITIVE_NUMBER = { min: {value: 0, message: "Minimum value is 0"} };
export const RULE_VALID_DECIMAL_NUMBER = {correctDecimal: (value) => isNumberWithDecimal(value) || "introduce a valid decimal number"}

//TODO: implement all functions
/**
 * check if value contains only numbers. not floating allowed
 * @param {value} value 
 * @returns 
 */
export const isNumberWithoutDecimal = (value) => {
    if (value == null) {
        return true;
    }
    return /^\d+$/.test(value);
}
/**
 * check if value is numeric including decimals sepated by "."
 * @param {value} value 
 * @returns 
 */
export const isNumberWithDecimal = (value) => {
    if (value == null) {
        return true;
    }
    return /^\d*\.?\d+$/.test(value);
}
/**
 * check if value is positive or greater than 0
 * @param {value} value 
 * @returns 
 */
export const isPositiveNumber = (value) => {
    if (value == null) {
        return true;
    }
    return Number(value) >= 0;
}
export const isFirstSmallerThanSecond = (first, second) => {
    if (first == null || second == null) {
        return true;
    }
    return Number(first) < Number(second);
}