#include<stdio.h>
#include<stdlib.h>

int main(void)
{
    int cant,pert=0,ext=0,inte=0;
    printf("Ingrese la cantidad de Pares ordenados que desea: ");
    scanf("%i",&cant);
    for(int cont = 1;cont<=cant;cont++)
    {
        int x,y;
        printf("Ingrese una coordenada para X entre -99 y 29 (excluyendo los limites): ");
        scanf("%i",&x);
        while(x<=-99 || x>=29)
        {
            printf("Ingrese una coordenada para X entre -99 y 29 (excluyendo los limites): ");
            scanf("%i",&x);
        }

        printf("Ingrese una para Y entre -99 y 29 (excluyendo los limites): ");
        scanf("%i",&y);
        while(y<=-99 || y>=29)
        {
            printf("Ingrese una coordenada para Y entre -99 y 29 (excluyendo los limites): ");
            scanf("%i",&y);
        }
        if(x<3 || x>8)
        {
            printf("La coordenada es exterior \n");
            ext+=1;
        }
        else if(x== 3 || x==8)
        {
            if(y<-5 || y>12)
            {
                printf("La coordenada es exterior \n");
                ext+=1;
            }
            else
            {
                printf("La coordenada pertenece \n");
                pert+=1;
            }
        }
        else
        {
            if(y<-5 || y>12)
            {
                printf("La coordenada es exterior \n");
                ext+=1;
            }
            else if(y==-5 || y==12)
            {
                printf("La coordenada pertenece \n");
                pert+=1;
            }
            else
            {
                printf("La coordenada es interior \n");
                inte+=1;
            }
        }

    }
    printf("Entre los %i Pares, %i fueron exteriores, %i pertenecian y %i eran interiores \n",cant,ext,pert,inte);
    return(0);
}