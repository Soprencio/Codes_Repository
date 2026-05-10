#include<stdio.h>
#include<stdlib.h>

int main()
{
    int num;
    printf("Ingrese un número entero: ");
    scanf("%i",&num);
    if(num%2==0)
    {
        printf("%i es par \n",num);
    }
    else
    {
        printf("%i es impar \n",num);
    }
    if(num>0)
    {
        printf("%i es mayor a 0 \n",num);
    }
    else
    {
        if(num==0)
        {
            printf("%i es 0 \n",num);
        }
        else
        {
            printf("%i es menor a 0 \n",num);
        }
    }
    int aux=0;
    for(int cont=2;cont<num;cont++)
    {
        if(num%cont==0)
        {
            aux=1;
        }
    }
    if(aux==0)
    {
        if(num!=1)
        {
            printf("%i es primo \n",num);
        }
        else
        {
            printf("%i no es primo \n",num);
        }
    }
    else
    {
        printf("%i no es primo \n",num);
    }
    return(0);

}