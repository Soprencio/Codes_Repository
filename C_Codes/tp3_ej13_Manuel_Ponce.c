#include<stdio.h>
#include<stdlib.h>

int main()
{
    int cant,x,y;
    printf("Ingrese la cantidad de pares ordenados: ");
    scanf("%i",&cant);
    for(int cont=1;cont<=cant;cont++)
    {
        printf("Ingrese un dato para X entre -99 y 99: ");
        scanf("%i",&x);
        printf("Ingrese un dato para Y entre -99 y 99: ");
        scanf("%i",&y);
        while(x<-99 || x>99)
        {
            printf("Ingrese un dato para X entre -99 y 99: ");
            scanf("%i",&x);
        }
        while(y<-99 || y>99)
        {
            printf("Ingrese un dato para Y entre -99 y 99: ");
            scanf("%i",&y);
        }
        if(x<-2 || x>5)
        {
            printf("La coordenada (%i;%i) es exterior \n",x,y);
        }
        else
        {
            if(x==-2 || x==5)
            {
                if(y>=2 || y<=4)
                {
                    printf("La coordenada (%i;%i) pertenece \n",x,y);
                }
                else
                {
                    printf("La coordenada (%i;%i) es exterior \n",x,y);
                }
            }
            else
            {
                if(y<2 || y>4)
                {
                    printf("La coordenada (%i;%i) es exterior \n",x,y);
                }
                else
                {
                    if(y==2 || y==4)
                    {
                        printf("La coordenada (%i;%i) pertenece \n",x,y);
                    }
                    else
                    {
                        printf("La coordenada (%i;%i) es interior \n",x,y);
                    }
                }
            }
        }
    }
    return(0);
}
