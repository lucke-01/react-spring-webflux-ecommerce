import { Alert, Box, Modal as ModalComponent } from "@mui/material";
import "./Modal.scss";
import Card from "../Card/Card";

const Modal = ({ children, title, visibility, handleOnClose, alert }) => {
  const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    maxHeight: "95vh",
    overflow: "auto",
  };

  return (
    <ModalComponent open={visibility}>
      <Box sx={style}>
        <div className="modal-component">
          <Card>
            <div className="modal-component__top">
              {title} <i className="bi bi-x" onClick={handleOnClose} />
            </div>
            {alert ? (
              <div className="modal-component__alert">
                <Alert severity="warning">{alert}</Alert>
              </div>
            ) : null}
            <div className="modal-component__content">{children}</div>
          </Card>
        </div>
      </Box>
    </ModalComponent>
  );
};

export default Modal;
