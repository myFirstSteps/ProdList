
package com.pankratov.prodlist.model.products;


public class ProductException extends Exception {
    ProductException(){
        
    }
    ProductException(String couse){
        super(couse);
    }
     ProductException(String couse,Throwable t){
        super(couse,t);
    }
}
