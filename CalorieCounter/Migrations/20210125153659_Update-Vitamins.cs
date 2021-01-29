using Microsoft.EntityFrameworkCore.Migrations;

namespace CalorieCounter.Migrations
{
    public partial class UpdateVitamins : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserVitamins_Vitamin_VitaminId",
                table: "UserVitamins");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Vitamin",
                table: "Vitamin");

            migrationBuilder.RenameTable(
                name: "Vitamin",
                newName: "Vitamins");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Vitamins",
                table: "Vitamins",
                column: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_UserVitamins_Vitamins_VitaminId",
                table: "UserVitamins",
                column: "VitaminId",
                principalTable: "Vitamins",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserVitamins_Vitamins_VitaminId",
                table: "UserVitamins");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Vitamins",
                table: "Vitamins");

            migrationBuilder.RenameTable(
                name: "Vitamins",
                newName: "Vitamin");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Vitamin",
                table: "Vitamin",
                column: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_UserVitamins_Vitamin_VitaminId",
                table: "UserVitamins",
                column: "VitaminId",
                principalTable: "Vitamin",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
