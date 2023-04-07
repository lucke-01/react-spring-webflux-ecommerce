import { forwardRef } from "react";
import { DateTimePicker } from "@mui/x-date-pickers/DateTimePicker";
import TextField from "@mui/material/TextField";

const DatePicker = forwardRef(({ error, helperText, ...props }, ref) => {
  return (
    <>
      <DateTimePicker
        ref={ref}
        {...props}
        ampm={false}
        inputFormat="DD/MM/YYYY HH:mm"
        renderInput={(params) => (
          <TextField
            name="test"
            {...params}
            size="small"
            error={error}
            helperText={helperText}
          />
        )}
      />
    </>
  );
});

export default DatePicker;
