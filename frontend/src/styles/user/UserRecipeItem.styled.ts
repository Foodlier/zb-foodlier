import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const MyRecipeList = styled.div`
  display: grid;
  justify-items: center;
  row-gap: 1rem;
  grid-template-columns: 1fr 1fr;
  ${breakpoints.large} {
    row-gap: 0;
    grid-template-columns: 1fr 1fr 1fr 1fr;
  }
`

export const RecipeCard = styled.div`
  display: inline-block;
  width: 90%;
  height: 280px;
  border-radius: 10px;
  padding: 16px;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
  cursor: pointer;
`

export const RecipeImg = styled.img`
  width: 100%;
  height: 60%;
  margin-bottom: 16px;
  border-radius: 10px 10px 0 0;
  background-color: aliceblue;
  object-fit: cover;
`

export const RecipeInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  height: 40%;
`

export const RecipeTopInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`

export const RecipeTitle = styled.span`
  font-weight: bold;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const LikeDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`
export const LikeCount = styled.span`
  font-size: 1.4rem;
`

export const RecipeContent = styled.p`
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`
export const NoRecipeCard = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 140px;
  border-radius: 10px;
  color: ${palette.divider};
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`
