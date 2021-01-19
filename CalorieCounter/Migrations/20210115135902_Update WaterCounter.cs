using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

namespace CalorieCounter.Migrations
{
    public partial class UpdateWaterCounter : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_WaterCounters",
                table: "WaterCounters");

            migrationBuilder.AddColumn<long>(
                name: "Id",
                table: "WaterCounters",
                type: "bigint",
                nullable: false,
                defaultValue: 0L)
                .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn);

            migrationBuilder.AddPrimaryKey(
                name: "PK_WaterCounters",
                table: "WaterCounters",
                column: "Id");

            migrationBuilder.CreateIndex(
                name: "IX_WaterCounters_UserId",
                table: "WaterCounters",
                column: "UserId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_WaterCounters",
                table: "WaterCounters");

            migrationBuilder.DropIndex(
                name: "IX_WaterCounters_UserId",
                table: "WaterCounters");

            migrationBuilder.DropColumn(
                name: "Id",
                table: "WaterCounters");

            migrationBuilder.AddPrimaryKey(
                name: "PK_WaterCounters",
                table: "WaterCounters",
                column: "UserId");
        }
    }
}
