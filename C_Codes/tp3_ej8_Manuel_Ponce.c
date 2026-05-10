#include<stdio.h>
#include<stdlib.h>

int main()
{
    long unsigned int num,fact=1;
    printf("Ingrese un número del 1 al 20: ");
    scanf("%li",&num);
    for(long unsigned int cont=1;cont<=num;cont++)
    {
        fact*=cont;
    }
    printf("El factorial de %li es %li \n",num,fact);
    return(0);
}