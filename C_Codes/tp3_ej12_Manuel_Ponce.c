#include<stdio.h>
#include<stdlib.h>

int main()
{
    int num,aux=0;
    printf("Ingrese un número entre 0 y 9: ");
    scanf("%i",&num);
    while(num<=-1 || num>=10)
    {
        printf("Ingrese un número entre 0 y 9: ");
        scanf("%i",&num);
    }
    for(int cont=0;cont<11;cont++)
    {
        aux=num*cont;
        printf("%i x %i es %i \n",num,cont,aux);
    }
    return(0);
}