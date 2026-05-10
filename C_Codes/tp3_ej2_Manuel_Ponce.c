#include<stdio.h>
#include<stdlib.h>

int main()
{
    int n1,n2,n3;
    printf("Ingrese 3 números enteros: ");
    scanf("%i",&n1);
    printf("Ingrese el segundo: ");
    scanf("%i",&n2);
    printf("Ingrese el tercero: ");
    scanf("%i",&n3);
    int may=n1;
    if(n2>may)
    {
        may=n2;
    }
    if(n3>may)
    {
        may=n3;
    }
    printf("El número mayor es %i \n",may);
    return(0);
}
