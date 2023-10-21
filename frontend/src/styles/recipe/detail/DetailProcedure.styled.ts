import styled from 'styled-components'
import {
  palette,
  breakpoints,
  typography,
  weight,
} from '../../../constants/Styles'

export const ProcedureContainer = styled.section`
  padding-bottom: 11rem;

  ${breakpoints.large} {
    margin-top: 5rem;
  }
`

export const MainTit = styled.h1`
  width: 100%;
  font-size: ${typography.mobile.mainTitle};
  font-size: ${weight.mainTitle};
  padding: 0.5rem 2rem;
  margin: 2rem 0 2rem;
  border-bottom: 0.2rem solid ${palette.divider};

  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
    margin-top: 0;
  }
`

export const ProcedureWrap = styled.ol`
  padding: 0 2rem;
`

export const ProcedureBox = styled.li`
  display: flex;
  justify-content: space-between;
  padding-bottom: 1rem;
`

export const ImgWrap = styled.div`
  display: flex;
  width: 10rem;
  height: 10rem;
  min-width: 10rem;
  min-height: 10rem;
  justify-content: center;
  align-items: center;
  margin-bottom: 2rem;
  border-radius: 1rem;
  overflow: hidden;
  margin-left: 1rem;

  ${breakpoints.large} {
    min-width: 15rem;
    min-height: 15rem;
  }
`

export const ProcedureImg = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
  border: 1px solid ${palette.divider};
  border-radius: inherit;
`

export const stepTxt = styled.p`
  font-size: ${typography.mobile.content};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`
