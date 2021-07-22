package com.pattern.factory.innercalss;



public class Factory implements ServiceFactory {

    private Service service;

    public Factory(ServiceFactory service){
        this.service=service.getServer();
    }


    @Override
    public Service getServer( ) {
        return service;
    }
}
