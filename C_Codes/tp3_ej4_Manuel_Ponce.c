#include<stdio.h>
#include<stdlib.h>

int main()
{
    int max, cant=0;
    printf("Ingrese un número entero: ");
    scanf("%i",&max);
    for(int cont=1;cont<=max;cont++)
    {
        if(cont%3==0)
        {
            cant+=1;
            printf("%i \n",cont);
        }
    }
    printf("La cantidad de múltiplos de 3 hasta %i es %i \n",max,cant);
    return(0);
}