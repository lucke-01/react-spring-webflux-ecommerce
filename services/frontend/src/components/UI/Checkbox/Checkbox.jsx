import { forwardRef } from "react";
import FormControlLabel from "@mui/material/FormControlLabel";
import { default as MuiCheckbox } from "@mui/material/Checkbox";
import "./Checkbox.scss";

const Checkbox = forwardRef(({ label, value, ...props }, ref) => {
  return (
    <FormControlLabel
      ref={ref}
      control={<MuiCheckbox {...props} />}
      checked={value}
      label={label}
    />
  );
});

export default Checkbox;
