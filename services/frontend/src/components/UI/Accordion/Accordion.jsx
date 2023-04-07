import { useState } from "react";

import "./Accordion.scss";
import Card from "../Card/Card";

const Accordion = ({ children, title }) => {
  const [isActive, setIsActive] = useState(false);

  return (
    <div className="accordion-component">
      <Card>
        <div
          className="accordion-component__top"
          onClick={() => setIsActive(!isActive)}
        >
          {title}{" "}
          <i
            className={
              isActive ? "bi bi-caret-down rotate" : "bi bi-caret-down"
            }
          />
        </div>
        {isActive && (
          <div className="accordion-component__content">{children}</div>
        )}
      </Card>
    </div>
  );
};

export default Accordion;
