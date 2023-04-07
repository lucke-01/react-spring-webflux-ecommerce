export const getErrorMessageHttp = (err) => {
    let error = "Something went wrong. Please try later.";
    if (err?.response?.data?.error != null) {
        error = err.response.data.error;
    } else if (err?.response?.data != null) {
        error = err.response.data;
    }
    return error;
}