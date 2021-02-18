using Microsoft.EntityFrameworkCore.Migrations;

namespace CalorieCounter.Migrations
{
    public partial class UpdateWaterSettings : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_WaterSettings_UserId",
                table: "WaterSettings");

            migrationBuilder.RenameColumn(
                name: "End",
                table: "WaterSettings",
                newName: "EndMinute");

            migrationBuilder.RenameColumn(
                name: "Begin",
                table: "WaterSettings",
                newName: "EndHour");

            migrationBuilder.AddColumn<int>(
                name: "BeginHour",
                table: "WaterSettings",
                type: "integer",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AddColumn<int>(
                name: "BeginMinute",
                table: "WaterSettings",
                type: "integer",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_WaterSettings_UserId",
                table: "WaterSettings",
                column: "UserId",
                unique: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_WaterSettings_UserId",
                table: "WaterSettings");

            migrationBuilder.DropColumn(
                name: "BeginHour",
                table: "WaterSettings");

            migrationBuilder.DropColumn(
                name: "BeginMinute",
                table: "WaterSettings");

            migrationBuilder.RenameColumn(
                name: "EndMinute",
                table: "WaterSettings",
                newName: "End");

            migrationBuilder.RenameColumn(
                name: "EndHour",
                table: "WaterSettings",
                newName: "Begin");

            migrationBuilder.CreateIndex(
                name: "IX_WaterSettings_UserId",
                table: "WaterSettings",
                column: "UserId");
        }
    }
}
