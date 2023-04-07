import React from 'react';
import toast from "react-hot-toast";
/**
 * Allow to do not break the app went a component error happens
 */
export class ErrorBoundary extends React.Component {
    constructor(props) {
      super(props);
      this.state = { hasError: false };
    }
  
    static getDerivedStateFromError(error) {
      return { hasError: true };
    }
    componentDidCatch(error, errorInfo) {
        toast.error("Something went wrong, Try it again later.", {
            position: "top-center",
          });
      //logErrorToMyService(error, errorInfo);
    }
    render() {
      if (this.state.hasError) {
        //return <h1>Something went wrong.</h1>;
        return this.props.children;
      }
      return this.props.children;
    }
}