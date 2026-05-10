#include<stdio.h>
#include<stdlib.h>

int main()
{
    int num,perf=0;
    printf("Ingrese un número entero: ");
    scanf("%i",&num);
    for(int cont=1;cont<num;cont++)
    {
        if(num%cont==0)
        {
            perf+=cont;
        }
    }
    if(num==perf)
    {
        printf("%i es perfecto \n",num);
    }
    else
    {
        printf("%i no es perfecto \n",num);
    }
    return(0);
}