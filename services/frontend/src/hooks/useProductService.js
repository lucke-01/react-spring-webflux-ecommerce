import { useContext } from "react";
import axios from "axios";
import {AuthContext} from "context";
import {BASE_API_URL} from "utils";

export const useProductService = () => {
    const { token } = useContext(AuthContext);
    const config = {
        headers: {
            Authorization: `Bearer ${token}`,
        }
    };
    const getProductByLocationId = async (idLocation) => {
        const url = `${BASE_API_URL}/product/?idLocation=${idLocation}`;
        return await axios.get(url, config).then(data => data.data);
    };
    const getProductById = async (idProduct) => {
        const url = `${BASE_API_URL}/product/${idProduct}`;
        return await axios.get(url, config).then(data => data.data);
    };
    const duplicateProduct = async (productToDuplicate) => {
        const url = `${BASE_API_URL}/product/duplicate`;
        return await axios.post(url, productToDuplicate, config).then(data => data.data);
    };
    
    return {
        getProductById,
        getProductByLocationId,
        duplicateProduct
    };
}