#include<stdio.h>
#include<stdlib.h>

int main()
{
    int fib1=1,fib0=0,aux;
    printf("%i \n",fib0);
    for(int secuen=1;secuen<20;secuen++)
    {
        printf("%i \n",fib1);
        aux=fib1;
        fib1+=fib0;
        fib0=aux;
    }
    return(0);
}