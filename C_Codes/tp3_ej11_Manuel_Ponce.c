#include<stdio.h>
#include<stdlib.h>

int main()
{
    int d,m,a;
    printf("Ingrese una fecha(día, mes y año en forma numérica) entre el primero de enero de 1950 y el 1 de enero de 2050 \n");
    printf("Ingrese día: ");
    scanf("%i",&d);
    printf("Ingrese mes: ");
    scanf("%i",&m);
    printf("Ingrese año: ");
    scanf("%i",&a);
    while(m<1 || m>12)
    {
        printf("Ingrese mes válido (1-12): ");
        scanf("%i",&m);
    }
    if(m==1 || m==3 || m==5 || m==7 || m==8 || m==10 || m==12)
    {
        while(d<1 || d>31)
        {
            printf("Ingrese día válido (1-31): ");
            scanf("%i",&d);
        }
    }
    else
    {
        if(m==2)
        {
            if((a%4==0 && a%100!=0) || (a%4==0 && a%100==0 && a%400==0))
            {
                while(d<1 || d>29)
                {
                    printf("Ingrese día válido (1-29): ");
                    scanf("%i",&d);
                }
            }
            else
            {
                while(d<1 || d>28)
                {
                    printf("Ingrese día válido (1-28): ");
                    scanf("%i",&d);
                }
            }
        }
        else
        {
            while(d<1 || d>30)
            {
                printf("Ingrese día válido (1-30): ");
                scanf("%i",&d);
            }
        }
    }
    if(a<2051 && a>1949)
    {
        if(a==2050)
        {
            if(d==1 && m==1)
            {
                printf("La fecha %i/%i/%i es válida \n",d,m,a);
            }
            else
            {
                printf("La fecha %i/%i/%i no es válida \n",d,m,a);
            }
        }
        else
        {
            printf("La fecha %i/%i/%i es válida \n",d,m,a);
        }
    }
    else
    {
        printf("La fecha %i/%i/%i no es válida \n",d,m,a);
    }
    return(0);
}