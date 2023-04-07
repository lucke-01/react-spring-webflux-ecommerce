import { forwardRef } from "react";
import TextField from "@mui/material/TextField";

const Input = forwardRef(({ ...props }, ref) => {
  return <TextField ref={ref} {...props} variant="outlined" size="small" />;
});

export default Input;
