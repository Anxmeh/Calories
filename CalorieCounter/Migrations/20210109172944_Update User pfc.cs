using Microsoft.EntityFrameworkCore.Migrations;

namespace CalorieCounter.Migrations
{
    public partial class UpdateUserpfc : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<double>(
                name: "UserCarbohydrate",
                table: "UserSettings",
                type: "double precision",
                nullable: false,
                defaultValue: 0.0);

            migrationBuilder.AddColumn<double>(
                name: "UserFat",
                table: "UserSettings",
                type: "double precision",
                nullable: false,
                defaultValue: 0.0);

            migrationBuilder.AddColumn<double>(
                name: "UserProtein",
                table: "UserSettings",
                type: "double precision",
                nullable: false,
                defaultValue: 0.0);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "UserCarbohydrate",
                table: "UserSettings");

            migrationBuilder.DropColumn(
                name: "UserFat",
                table: "UserSettings");

            migrationBuilder.DropColumn(
                name: "UserProtein",
                table: "UserSettings");
        }
    }
}
