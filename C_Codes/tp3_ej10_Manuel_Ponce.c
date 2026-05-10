#include<stdio.h>
#include<stdlib.h>

int main()
{
    int num1, num2, aux1=0, aux2=0;
    printf("Ingrese un número entero: ");
    scanf("%i",&num1);
    printf("Ingrese otro número entero: ");
    scanf("%i",&num2);
    for(int cont1=1;cont1<num1;cont1++)
    {
        if(num1%cont1==0)
        {
            aux1+=cont1;
        }
    }
    for(int cont2=1;cont2<num2;cont2++)
    {
        if(num2%cont2==0)
        {
            aux2+=cont2;
        }
    }
    if(aux2==num1 && aux1==num2)
    {
        printf("%i y %i son números amigos \n",num1,num2);
    }
    else
    {
        printf("%i y %i no son números amigos \n",num1,num2);
    }
    return(0);
}