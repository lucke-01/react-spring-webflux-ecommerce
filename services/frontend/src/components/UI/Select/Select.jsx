import { forwardRef } from "react";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import { default as MuiSelect } from "@mui/material/Select";
import { FormHelperText } from "@mui/material";

const Select = forwardRef(
  (
    {
      data,
      value,
      onChange,
      label,
      error,
      helperText,
      disabled = false,
      fullWidth = true,
      className,
      ...others
    },
    ref
  ) => {
    return (
      <FormControl
        size="small"
        fullWidth={fullWidth}
        error={error}
        className={className}
        {...others}
      >
        <InputLabel>{label}</InputLabel>
        <MuiSelect
          ref={ref}
          value={value}
          label={label}
          onChange={onChange}
          disabled={disabled}
        >
          {data?.map(({ value, label }) => (
            <MenuItem key={value} value={value}>
              {label}
            </MenuItem>
          ))}
        </MuiSelect>
        {helperText ? <FormHelperText>{helperText}</FormHelperText> : null}
      </FormControl>
    );
  }
);

export default Select;
