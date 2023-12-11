import Customer from './Customer';
import Product from './Product';

export default interface Purchase { 
    orderId: string; 
    customer: Customer; 
    product: Product;
}