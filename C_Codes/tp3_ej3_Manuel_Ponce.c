#include<stdio.h>
#include<stdlib.h>

int main()
{
    int suma=0;
    for(int cont=1;cont<101;cont++)
    {
        suma+=cont;
    }
    printf("La suma de los 100 primeros números es %i \n",suma);
    return(0);
}