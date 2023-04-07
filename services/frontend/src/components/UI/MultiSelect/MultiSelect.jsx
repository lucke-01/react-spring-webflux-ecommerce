import { forwardRef } from "react";
import OutlinedInput from "@mui/material/OutlinedInput";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { FormHelperText } from "@mui/material";

import "./MultiSelect.scss";

const MultiSelect = forwardRef(({ data, text, value, helperText, ...rest }, ref) => {
  return (
    <FormControl size="small">
      <InputLabel id="demo-multiple-checkbox-label">{text}</InputLabel>
      <Select
        ref={ref}
        multiple
        value={value}
        input={<OutlinedInput label={text} />}
        renderValue={(selected) => {
          const values = selected.map((a) => a.label);
          return values.join(", ");
        }}
        {...rest}
      >
        {data.map((item) => (
          <MenuItem key={item.value} value={item}>
            {item.label}
          </MenuItem>
        ))}
      </Select>
      {helperText ? <FormHelperText className="multiselect-helper-text">{helperText}</FormHelperText> : null}
    </FormControl>
  );
});

export default MultiSelect;
